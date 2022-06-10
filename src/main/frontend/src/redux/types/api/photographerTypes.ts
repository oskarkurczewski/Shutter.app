import internal from "stream";

export interface photographerTableEntry {
    login: string,
    email: string,
    name: string,
    surname: string,
    reviewCount: number,
    score: number,
    specializations: string[],
    longitude: number,
    latitude: number
}

export interface getPhotographersListRequest {
    pageNo: number,
    recordsPerPage: number,
}