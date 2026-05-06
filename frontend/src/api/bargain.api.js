import { http } from "./http";

export async function sendBargainChat(sessionId, message, buyerOffer) {
  const payload = { message };

  if (buyerOffer !== null && buyerOffer !== undefined && buyerOffer !== "") {
    payload.buyerOffer = Number(buyerOffer);
  }

  const res = await http.post(`/api/chatbot/${sessionId}/chat`, payload);
  return res.data;
}