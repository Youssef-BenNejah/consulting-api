import http from "k6/http";
import { check, sleep } from "k6";

export const options = {
  vus: 1,
  duration: "30s",
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";

export default function () {
  let res = http.get(`${BASE_URL}/api/v1/dashboard/summary`);
  check(res, { "summary 200": (r) => r.status === 200 });

  res = http.get(`${BASE_URL}/api/v1/clients`);
  check(res, { "clients 200": (r) => r.status === 200 });

  res = http.get(`${BASE_URL}/api/v1/projects`);
  check(res, { "projects 200": (r) => r.status === 200 });

  res = http.get(`${BASE_URL}/api/v1/invoices`);
  check(res, { "invoices 200": (r) => r.status === 200 });

  res = http.get(`${BASE_URL}/api/v1/reports/overview`);
  check(res, { "reports 200": (r) => r.status === 200 });

  sleep(1);
}
