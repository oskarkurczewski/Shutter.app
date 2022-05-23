import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";

import LoginPage from "pages/login";
import DashboardPage from "pages/dashboard";
import PageLayout from "pages/layout";
import Homepage from "pages/homepage";
import NotFound404 from "pages/not-found";
import { useAppDispatch } from "redux/hooks";
import { getLoginPayload, getTokenExp } from "util/loginUtil";
import { login } from "redux/slices/authSlice";
import ProtectedRoute from "components/routes/protected-route";
import { AccessLevel } from "types/AccessLevel";
import RegisterPage from "pages/register";
import CreateAccountPage from "pages/users/create";
import AccountListPage from "pages/users/list";
import EditAccountPage from "pages/users/edit";
import SettingsPage from "pages/settings";
import ResetPasswordPage from "pages/reset-password";
import ConfirmRegistrationPage from "pages/confirmRegistration";
import UnblockOwnAccountPage from "pages/token-based/unblock-own-account";

function App() {
   const dispatch = useAppDispatch();
   if (localStorage.getItem("token") && Date.now() < getTokenExp()) {
      dispatch(login(getLoginPayload()));
   } else {
      localStorage.setItem("accessLevel", "GUEST");
   }

   return (
      <BrowserRouter>
         <Routes>
            <Route path="*" element={<NotFound404 />} />

            <Route element={<PageLayout />}>
               <Route path="" element={<Homepage />} />

               <Route
                  path="login"
                  element={
                     <ProtectedRoute roles={[AccessLevel.GUEST]}>
                        <LoginPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path="register"
                  element={
                     <ProtectedRoute roles={[AccessLevel.GUEST]}>
                        <RegisterPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path="/confirm-registration/:registerationToken"
                  element={
                     <ProtectedRoute roles={[AccessLevel.GUEST]}>
                        <ConfirmRegistrationPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path="reset-password"
                  element={
                     <ProtectedRoute roles={[AccessLevel.GUEST]}>
                        <ResetPasswordPage />
                     </ProtectedRoute>
                  }
               />

               <Route
                  path="settings"
                  element={
                     <ProtectedRoute
                        roles={[
                           AccessLevel.ADMINISTRATOR,
                           AccessLevel.MODERATOR,
                           AccessLevel.PHOTOGRAPHER,
                           AccessLevel.CLIENT,
                        ]}
                     >
                        <SettingsPage />
                     </ProtectedRoute>
                  }
               ></Route>

               <Route path="users">
                  <Route
                     path=""
                     element={
                        <ProtectedRoute
                           roles={[AccessLevel.ADMINISTRATOR, AccessLevel.MODERATOR]}
                        >
                           <AccountListPage />
                        </ProtectedRoute>
                     }
                  />

                  <Route
                     path="create"
                     element={
                        <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                           <CreateAccountPage />
                        </ProtectedRoute>
                     }
                  />

                  <Route
                     path=":id/edit"
                     element={
                        <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                           <EditAccountPage />
                        </ProtectedRoute>
                     }
                  />
               </Route>

               <Route path="dashboard" element={<DashboardPage />} />

               {/* Token-based routes */}
               <Route path="unblock-account/:token" element={<UnblockOwnAccountPage />} />
            </Route>
         </Routes>
      </BrowserRouter>
   );
}

export const apiUrl = "http://studapp.it.p.lodz.pl:8002/api";

export default App;
