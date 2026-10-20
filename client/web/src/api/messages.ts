import { apiClient } from "@/api/client.ts";
import type { MessagesResponse } from "./schemas/messages.ts";

export async function get_messages(): Promise<MessagesResponse> {
  const res = await apiClient.get<MessagesResponse>("/messages");
  return res.data;
}
