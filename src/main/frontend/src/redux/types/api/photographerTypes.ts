import { basicUserInfoResponse } from "./accountTypes";
import { ReviewReportCause } from "types/ReviewReportCause";

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
   cause: ReviewReportCause;
}
