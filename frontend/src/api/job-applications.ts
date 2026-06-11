import { http } from "./http";
import type { JobApplication } from "../types/job-application";
import type { PageResponse } from "../types/pagination";

export async function getJobApplications(): Promise<PageResponse<JobApplication>> {
  const response = await http.get<PageResponse<JobApplication>>(
    "/job-applications?page=0&size=10&sortBy=createdAt&direction=desc"
  );

  return response.data;
}