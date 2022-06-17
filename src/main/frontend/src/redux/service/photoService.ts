import { api } from "./api"
import { postPhotoRequest } from "redux/types/api/photoTypes";

const PhotoService = api.injectEndpoints({
    endpoints: (builder) => ({
        postPhotoRequest: builder.mutation<
            void,
            postPhotoRequest
        >({
            query: (data) => ({ url: "profile/photo", method: "POST", body: data })
        })
    })
});

export const {
    usePostPhotoRequestMutation
} = PhotoService;