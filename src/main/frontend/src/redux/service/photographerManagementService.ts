import { api } from "./api"
import { getPhotographersListRequest, photographerTableEntry } from "redux/types/api/photographerTypes"
import { getListResponse } from "redux/types/api/dataTypes";

const PhotographerManagementService = api.injectEndpoints({
    endpoints: (builder) => ({
        getActivePhotographers: builder.mutation<
            getListResponse<photographerTableEntry>,
            getPhotographersListRequest
        >({
            query: (data) => ({ url: "reservation/photographers", params: data })
        })
    })
});

export const {
    useGetActivePhotographersMutation
} = PhotographerManagementService;