import { basicUserInfoResponse } from "./accountTypes";
import { ReviewReportCause } from "types/ReviewReportCause";
import { DateTime } from "luxon";

export interface basicPhotographerInfo extends basicUserInfoResponse {
   score: number;
   reviewCount: number;
   description: string;
   latitude: number;
   longitude: number;
   specializationList: string[];
}

export interface photographerTableEntry {
   login: string;
   email: string;
   name: string;
   surname: string;
   reviewCount: number;
   score: number;
   specializations: string[];
   longitude: number;
   latitude: number;
}

export interface getPhotographersListRequest {
   pageNo: number;
   recordsPerPage: number;
}

export interface reportPhotographerReviewRequest {
   reviewId: number;
   cause: string;
}

export interface getPhotographerReviewsResponse {
   pageNo: number;
   recordsPerPage: number;
   list: {
      id: number;
      name: string;
      surname: string;
      score: number;
      content: string;
      likeCount: string;
   }[];
}

export interface getPhotographerReviewsRequest {
   pageNo: number;
   recordsPerPage?: number;
   photographerLogin: string;
}

export interface AvailabilityResponse {
   id: number;
   day: number;
   from: string;
   to: string;
}

export interface AvailabilityRequest {
   day: string;
   from: string;
   to: string;
}

export interface AddReservationRequest {
   photographerLogin: string;
   from: DateTime;
   to: DateTime;
}

export interface ReservationRequest {
   name?: string;
   order?: "asc" | "desc";
   all?: boolean;
   date: string;
}

export interface UserData {
   login: string;
   name: string;
   surname: string;
   email: string;
}

export interface ReservationResponse {
   id: number;
   photographer: UserData;
   client: UserData;
   from: string;
   to: string;
}

export interface ReservationCalendarEntryResponse {
   id: number;
   from: string;
   to: string;
}

export interface CalendarReservationResponse {
   id: number;
   from: string;
   to: string;
}
