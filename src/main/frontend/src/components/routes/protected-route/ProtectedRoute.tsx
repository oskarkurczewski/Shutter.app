import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "redux/hooks";
import { AccessLevel } from "types/AccessLevel";
import { motion } from "framer-motion";

interface Props {
   roles: AccessLevel[];
   children: React.ReactNode;
}

export const ProtectedRoute = ({ roles, children }: Props) => {
   const navigate = useNavigate();
   const level = useAppSelector((state) => state.auth.accessLevel);

   useEffect(() => {
      if (!roles.includes(level)) {
         if (level == AccessLevel.GUEST) {
            navigate("/login");
         } else {
            navigate("/");
         }
      }
   }, [level]);

   return <>{children}</>;
};
