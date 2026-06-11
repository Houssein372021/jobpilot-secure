import { http } from "./http";
import type { DashboardStats } from "../types/dashboard";

export async function getDashboardStats(): Promise<DashboardStats> {
  const response = await http.get<DashboardStats>("/dashboard/stats");
  return response.data;
}