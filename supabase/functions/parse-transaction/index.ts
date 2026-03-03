import { serve } from "https://deno.land/std@0.168.0/http/server.ts"
import { GoogleGenerativeAI } from "https://esm.sh/@google/generative-ai@0.1.0"

const corsHeaders = {
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Headers': 'authorization, x-client-info, apikey, content-type',
}

serve(async (req) => {
  if (req.method === 'OPTIONS') {
    return new Response('ok', { headers: corsHeaders })
  }

  try {
    const { text } = await req.json()
    const genAI = new GoogleGenerativeAI(Deno.env.get('GEMINI_API_KEY')!)
    const model = genAI.getGenerativeModel({ model: "gemini-2.0-flash-lite" })

    const prompt = `
      Você é um assistente financeiro brasileiro especializado em processar transações.
      Extraia as informações do texto abaixo e retorne APENAS um JSON puro.
      
      Texto: "${text}"
      Data Atual: ${new Date().toISOString()}

      Categorias permitidas: alimentacao, transporte, moradia, saude, lazer, educacao, vestuario, servicos, investimentos, salario, freelance, outros.
      Tipos permitidos: income (receita), expense (despesa).
      Métodos permitidos: pix, credito, debito, dinheiro, boleto, transferencia.

      Regras:
      - "recebi", "ganhei", "salário" -> type: income
      - "paguei", "gastei", "comprei" -> type: expense
      - Detecte o valor numérico.
      - Se mencionar "parcelado", defina payment_method como "credito" e extraia o número de parcelas.
      - Retorne a data no formato YYYY-MM-DD.

      Formato de Saída (JSON):
      {
        "type": "expense" | "income",
        "amount": number,
        "category": "string",
        "description": "string",
        "date": "YYYY-MM-DD",
        "payment_method": "string",
        "installments": number | null
      }
    `

    const result = await model.generateContent(prompt)
    const response = await result.response
    const jsonText = response.text().replace(/```json|```/g, "").trim()
    const data = JSON.parse(jsonText)

    return new Response(JSON.stringify(data), {
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
    })
  } catch (error) {
    return new Response(JSON.stringify({ error: error.message }), {
      status: 400,
      headers: { ...corsHeaders, 'Content-Type': 'application/json' },
    })
  }
})
