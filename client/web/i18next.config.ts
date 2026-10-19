import { defineConfig } from "i18next-cli";

export default defineConfig({
  locales: ["tr", "en"],
  extract: {
    input: ["src/**/*.{ts,tsx}", "!src/i18n/**", "!src/locales/**"],
    output: "src/locales/{{language}}/{{namespace}}.json",
    defaultNS: "common",
    primaryLanguage: "tr",
    preservePatterns: [],
  },
});
