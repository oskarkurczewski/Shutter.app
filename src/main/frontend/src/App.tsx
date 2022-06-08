import React, { Suspense, useEffect } from "react";
import "./style.scss";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "redux/hooks";

import { ProtectedRoute } from "components/routes";
import { PageLayout } from "layout";
import { Button } from "components/shared";
import { getLoginPayload, getTokenExp } from "util/loginUtil";
import { AccessLevel } from "types/AccessLevel";
import { login, logout } from "redux/slices/authSlice";
import { push, remove } from "redux/slices/toastSlice";
import { useRefreshTokenMutation } from "redux/service/api";

import { DashboardPage } from "pages/dashboard";
import { HomePage } from "pages/home";
import { NotFoundPage } from "pages/not-found";
import { RegisterPage, LoginPage } from "pages/auth";
import { SettingsPage } from "pages/settings";
import { ResetPasswordPage } from "pages/reset-password";
import { RequestResetPasswordPage } from "pages/request-reset-password";
import * as UserPages from "pages/users";
import * as TokenBased from "pages/token-based";

function App() {
   const dispatch = useAppDispatch();
   const exp = useAppSelector((state) => state.auth.exp);
   const [refreshToken] = useRefreshTokenMutation();

   if (localStorage.getItem("token") && Date.now() < getTokenExp()) {
      dispatch(login(getLoginPayload()));
   } else {
      dispatch(logout());
   }

   const data = {
      name: "refreshToken",
      label: "Powiadomienie",
      text: "Twoja sesja niedługo wygaśnie, kliknij w przycisk poniżej, aby ją przedłużyć!",
      content: (
         <Button
            onClick={async (e) => {
               e.preventDefault();
               try {
                  const token = await refreshToken({}).unwrap();
                  localStorage.setItem("token", token.token);
                  dispatch(login(getLoginPayload()));
                  dispatch(remove("refreshToken"));
               } catch (err) {
                  return;
               }
            }}
         >
            Przedłuż
         </Button>
      ),
   };

   useEffect(() => {
      if (exp !== 0 && exp - Date.now() < 1000 * 60 * 2) dispatch(push(data));

      const timeoutID = setInterval(() => {
         if (exp !== 0 && exp - Date.now() < 1000 * 60 * 2) {
            dispatch(push(data));
         }
      }, 1000 * 60 * 0.5);

      return () => {
         clearTimeout(timeoutID);
      };
   }, [exp]);

   return (
      <Suspense fallback="loading...">
         <BrowserRouter>
            <Routes>
               <Route path="*" element={<NotFoundPage />} />

               <Route element={<PageLayout />}>
                  <Route path="" element={<HomePage />} />

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
                     path="request-reset-password"
                     element={
                        <ProtectedRoute roles={[AccessLevel.GUEST]}>
                           <RequestResetPasswordPage />
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
                              <UserPages.UserAccountListPage />
                           </ProtectedRoute>
                        }
                     />

                     <Route
                        path="create"
                        element={
                           <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                              <UserPages.CreateUserAccountPage />
                           </ProtectedRoute>
                        }
                     />

                     <Route
                        path=":login/edit"
                        element={
                           <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                              <UserPages.EditUserAccountPage />
                           </ProtectedRoute>
                        }
                     />

                     <Route
                        path=":login/info"
                        element={
                           <ProtectedRoute
                              roles={[AccessLevel.ADMINISTRATOR, AccessLevel.MODERATOR]}
                           >
                              <UserPages.UserAccountInfoPage />
                           </ProtectedRoute>
                        }
                     />
                  </Route>

                  <Route path="dashboard" element={<DashboardPage />} />

                  {/* Token-based routes */}
                  <Route
                     path="change-own-email/:token"
                     element={<TokenBased.ChangeOwnEmailPage />}
                  />

                  <Route
                     path="unblock-account/:token"
                     element={
                        <ProtectedRoute roles={[AccessLevel.GUEST]}>
                           <TokenBased.UnblockOwnAccountPage />
                        </ProtectedRoute>
                     }
                  />

                  <Route
                     path="/confirm-registration/:token"
                     element={
                        <ProtectedRoute roles={[AccessLevel.GUEST]}>
                           <TokenBased.ConfirmRegistrationPage />
                        </ProtectedRoute>
                     }
                  />

                  <Route
                     path="password-reset/:token"
                     element={
                        <ProtectedRoute roles={[AccessLevel.GUEST]}>
                           <ResetPasswordPage />
                        </ProtectedRoute>
                     }
                  />
               </Route>
            </Routes>
         </BrowserRouter>
      </Suspense>
   );
}

export const apiUrl = "https://studapp.it.p.lodz.pl:8002/api";

export default App;
