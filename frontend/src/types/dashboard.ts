export type DashboardStats = {
  total: number;
  saved: number;
  applied: number;
  interview: number;
  offer: number;
  rejected: number;
  withdrawn: number;
  favorites: number;
};

export type DashboardActionSummary = {
  todayFollowUps: number;
  overdueFollowUps: number;
  upcomingFollowUps: number;
  applicationsWithoutFollowUp: number;
  savedApplications: number;
  interviewApplications: number;
};