import { useQuery } from "@tanstack/react-query";
import api from "../api/api";

// ------------------------------------------------------
// Fetch My Short URLs
// ------------------------------------------------------
export const useFetchMyShortUrls = (token, onError) => {
  return useQuery({
    queryKey: ["my-shortenurls", token],
    queryFn: async () => {
      return await api.get("/api/urls/myurls", {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
          Authorization: "Bearer " + token,
        },
      });
    },
    select: (data) => {
      const sortedData = data.data.sort(
        (a, b) => new Date(b.createdDate) - new Date(a.createdDate)
      );
      return sortedData;
    },
    onError,
    staleTime: 5000,
  });
};

// ------------------------------------------------------
// Fetch Total Clicks
// ------------------------------------------------------
export const useFetchTotalClicks = (token, onError) => {
  return useQuery({
    queryKey: ["url-totalclick", token],
    queryFn: async () => {
      return await api.get(
        "/api/urls/totalClicks?startDate=2025-01-01&endDate=2026-12-31",
        {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: "Bearer " + token,
          },
        }
      );
    },
    select: (data) => {
      // Convert object {date: count} → array
      return Object.keys(data.data).map((key) => ({
        clickDate: key,
        count: data.data[key],
      }));
    },
    onError,
    staleTime: 5000,
  });
};
