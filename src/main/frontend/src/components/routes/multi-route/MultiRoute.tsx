import React, { ReactNode, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";

type Route = {
   [key in AccessLevel]?: ReactNode;
};

interface Props {
   routes: Route;
}

const MultiRoute = ({ routes }: Props) => {
   const navigate = useNavigate();
   const level = useAppSelector((state) => state.auth.accessLevel);

   useEffect(() => {
      if (!routes[level]) {
         navigate(-1);
      }
   }, []);

   return <>{routes[level]}</>;
};

export default MultiRoute;
