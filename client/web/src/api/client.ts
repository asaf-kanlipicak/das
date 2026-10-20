import axios, { type AxiosInstance } from "axios";

const baseURL = import.meta.env.VITE_API_URL;

const apiClient: AxiosInstance = axios.create({
  baseURL,
  withCredentials: true,
  headers: {
    Accept: "application/json",
  },
  timeout: 180_000,
  paramsSerializer: {
    indexes: null,
  },
});

export { apiClient };
