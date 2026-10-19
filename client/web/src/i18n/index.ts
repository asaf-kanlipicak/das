import i18n from "i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import { initReactI18next } from "react-i18next";

import enCommon from "../locales/en/common.json";

import trCommon from "../locales/tr/common.json";

const resources = {
  en: { common: enCommon },
  tr: { common: trCommon },
};

export const defaultNS = "common";

i18n.on("languageChanged", (lng: string) => {
  document.documentElement.lang = lng;
});

void i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources,
    load: "languageOnly",
    fallbackLng: "tr",
    supportedLngs: ["tr", "en"],
    defaultNS,
    ns: ["common"],
    interpolation: {
      escapeValue: false,
    },
    detection: {
      order: ["localStorage", "navigator"],
    },
  });

export default i18n;
