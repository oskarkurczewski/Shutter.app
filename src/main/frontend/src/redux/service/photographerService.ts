import { api } from "./api";
import { basicPhotographerInfo } from "redux/types/api/photographerTypes";

const PhotographerService = api.injectEndpoints({
   endpoints: (builder) => ({
      getPhotographerDetailedInfo: builder.query<basicPhotographerInfo, string>({
         query: (login) => ({ url: `/photographer/${login}/info` }),
      }),
   }),
});

export const { useGetPhotographerDetailedInfoQuery } = PhotographerService;
