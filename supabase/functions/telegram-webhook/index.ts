import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { createClient } from "https://esm.sh/@supabase/supabase-js@2.38.4"

const TELEGRAM_TOKEN = Deno.env.get('TELEGRAM_BOT_TOKEN')
const SUPABASE_URL = Deno.env.get('SUPABASE_URL')
const SUPABASE_SERVICE_ROLE_KEY = Deno.env.get('SUPABASE_SERVICE_ROLE_KEY')

const supabase = createClient(SUPABASE_URL!, SUPABASE_SERVICE_ROLE_KEY!)

serve(async (req) => {
    try {
        const update = await req.json()
        const chatId = update.message?.chat.id
        const text = update.message?.text

        if (!text) return new Response("ok")

        if (text === "/start") {
            await sendTelegramMessage(chatId, "Bem-vindo ao FinançasIA! 🚀\nUse /vincular CODIGO para conectar sua conta.")
        } else if (text.startsWith("/vincular")) {
            const code = text.split(" ")[1]
            // Lógica de vinculação aqui...
            await sendTelegramMessage(chatId, "Conta vinculada com sucesso! ✅")
        } else {
            // Processar com Gemini (mesmo fluxo da parse-transaction)
            // e salvar no banco
            await sendTelegramMessage(chatId, "Recebido! Processando sua transação com IA... 🤖")
        }

        return new Response("ok")
    } catch (e) {
        return new Response(e.message, { status: 500 })
    }
})

async function sendTelegramMessage(chatId: number, text: string) {
    await fetch(`https://api.telegram.org/bot${TELEGRAM_TOKEN}/sendMessage`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ chat_id: chatId, text: text })
    })
}
