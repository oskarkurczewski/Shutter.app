import React, { Suspense, useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./style.scss";
import DashboardPage from "pages/dashboard";
import PageLayout from "pages/layout";
import Homepage from "pages/homepage";
import NotFound404 from "pages/not-found";
import { useAppDispatch, useAppSelector } from "redux/hooks";
import { getLoginPayload, getTokenExp } from "util/loginUtil";
import { login, logout } from "redux/slices/authSlice";
import ProtectedRoute from "components/routes/protected-route";
import { AccessLevel } from "types/AccessLevel";
import RegisterPage from "pages/register";
import CreateAccountPage from "pages/users/create";
import AccountListPage from "pages/users/list";
import EditAccountPage from "pages/users/edit";
import SettingsPage from "pages/settings";
import ResetPasswordPage from "pages/reset-password";
import UnblockOwnAccountPage from "pages/token-based/unblock-own-account";
import LoginPage from "pages/login";
import Button from "components/shared/Button";
import { push, remove } from "redux/slices/toastSlice";
import { useRefreshTokenMutation } from "redux/service/api";
import RequestResetPasswordPage from "pages/request-reset-password";
import ConfirmRegistrationPage from "pages/token-based/confirm-registration";
import ChangeOwnEmailPage from "pages/token-based/change-own-email";

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
                     path="/confirm-registration/:token"
                     element={
                        <ProtectedRoute roles={[AccessLevel.GUEST]}>
                           <ConfirmRegistrationPage />
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
                        path=":login/edit"
                        element={
                           <ProtectedRoute roles={[AccessLevel.ADMINISTRATOR]}>
                              <EditAccountPage />
                           </ProtectedRoute>
                        }
                     />
                  </Route>

                  <Route path="dashboard" element={<DashboardPage />} />

                  {/* Token-based routes */}
                  <Route
                     path="unblock-account/:token"
                     element={<UnblockOwnAccountPage />}
                  />

                  <Route
                     path="change-own-email/:token"
                     element={<ChangeOwnEmailPage />}
                  />
               </Route>
            </Routes>
         </BrowserRouter>
      </Suspense>
   );
}

export const apiUrl = "https://studapp.it.p.lodz.pl:8002/api";

export default App;
