-- ENUMS
CREATE TYPE transaction_type AS ENUM ('income', 'expense');
CREATE TYPE transaction_category AS ENUM (
    'alimentacao', 'transporte', 'moradia', 'saude', 'lazer', 
    'educacao', 'vestuario', 'servicos', 'investimentos', 
    'salario', 'freelance', 'outros'
);
CREATE TYPE family_role AS ENUM ('owner', 'member');
CREATE TYPE invitation_status AS ENUM ('pending', 'accepted', 'rejected');

-- FAMILY GROUPS
CREATE TABLE family_groups (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- FAMILY MEMBERS
CREATE TABLE family_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    family_group_id UUID REFERENCES family_groups(id) ON DELETE CASCADE,
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    role family_role DEFAULT 'member',
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(family_group_id, user_id)
);

-- CREDIT CARDS
CREATE TABLE credit_cards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    family_group_id UUID REFERENCES family_groups(id) ON DELETE SET NULL,
    name TEXT NOT NULL,
    brand TEXT NOT NULL,
    color TEXT NOT NULL,
    credit_limit NUMERIC NOT NULL DEFAULT 0,
    closing_day INTEGER NOT NULL CHECK (closing_day BETWEEN 1 AND 31),
    due_day INTEGER NOT NULL CHECK (due_day BETWEEN 1 AND 31),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- TRANSACTIONS
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    family_group_id UUID REFERENCES family_groups(id) ON DELETE SET NULL,
    type transaction_type NOT NULL,
    amount NUMERIC NOT NULL CHECK (amount > 0),
    category transaction_category DEFAULT 'outros',
    description TEXT NOT NULL,
    date DATE DEFAULT CURRENT_DATE,
    payment_method TEXT,
    credit_card_id UUID REFERENCES credit_cards(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- INSTALLMENTS (LOGIC)
CREATE TABLE installments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    credit_card_id UUID REFERENCES credit_cards(id) ON DELETE CASCADE,
    transaction_id UUID REFERENCES transactions(id) ON DELETE CASCADE,
    total_installments INTEGER NOT NULL CHECK (total_installments > 1),
    current_installment INTEGER NOT NULL,
    installment_amount NUMERIC NOT NULL,
    start_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- FINANCIAL GOALS
CREATE TABLE financial_goals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    family_group_id UUID REFERENCES family_groups(id) ON DELETE SET NULL,
    name TEXT NOT NULL,
    target_amount NUMERIC NOT NULL,
    current_amount NUMERIC DEFAULT 0,
    goal_type TEXT NOT NULL, -- 'savings' ou 'limit'
    category transaction_category,
    deadline DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- TELEGRAM
CREATE TABLE telegram_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE UNIQUE,
    telegram_chat_id BIGINT UNIQUE,
    telegram_username TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE telegram_link_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES auth.users(id) ON DELETE CASCADE,
    code TEXT NOT NULL UNIQUE,
    expires_at TIMESTAMP WITH TIME ZONE DEFAULT (NOW() + INTERVAL '10 minutes'),
    used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- RLS FUNCTIONS
CREATE OR REPLACE FUNCTION is_family_member(_user_id UUID, _group_id UUID)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1 FROM family_members 
        WHERE user_id = _user_id AND family_group_id = _group_id
    );
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- RLS POLICIES

-- Transactions
ALTER TABLE transactions ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Users can see own or family transactions" ON transactions
    FOR ALL USING (auth.uid() = user_id OR is_family_member(auth.uid(), family_group_id));

-- Credit Cards
ALTER TABLE credit_cards ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Users can see own or family cards" ON credit_cards
    FOR ALL USING (auth.uid() = user_id OR is_family_member(auth.uid(), family_group_id));

-- Goals
ALTER TABLE financial_goals ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Users can see own or family goals" ON financial_goals
    FOR ALL USING (auth.uid() = user_id OR is_family_member(auth.uid(), family_group_id));

-- Family Groups
ALTER TABLE family_groups ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Members can see group" ON family_groups
    FOR SELECT USING (is_family_member(auth.uid(), id));
CREATE POLICY "Owners can manage group" ON family_groups
    FOR ALL USING (owner_id = auth.uid());

-- Family Members
ALTER TABLE family_members ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Members can see other members" ON family_members
    FOR SELECT USING (is_family_member(auth.uid(), family_group_id));
