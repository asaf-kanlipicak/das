export interface MessageRecord {
  index: number;
  content: string;
}
export interface MessagesResponse {
  messages: MessageRecord[];
}
