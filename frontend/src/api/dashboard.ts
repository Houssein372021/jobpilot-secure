import { http } from "./http";
import type { DashboardActionSummary, DashboardStats } from "../types/dashboard";

export async function getDashboardStats(): Promise<DashboardStats> {
  const response = await http.get<DashboardStats>("/dashboard/stats");
  return response.data;
}

export async function getDashboardActionSummary(): Promise<DashboardActionSummary> {
  const response = await http.get<DashboardActionSummary>("/dashboard/action-summary");
  return response.data;
}