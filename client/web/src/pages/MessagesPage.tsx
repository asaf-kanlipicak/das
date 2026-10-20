import { useQuery } from "@tanstack/react-query";
import { useTranslation } from "react-i18next";
import { get_messages } from "@/api/messages.ts";

export default function MessagesPage() {
  const { t } = useTranslation("common");
  const { data } = useQuery({
    queryKey: ["messages"],
    queryFn: () => get_messages(),
  });
  return (
    <div>
      <table>
        <thead>
          <tr>
            <td>{t("message")}</td>
          </tr>
        </thead>
        <tbody>
          {data?.messages.map((message) => (
            <tr key={message.index}>
              <td>{message.content}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
