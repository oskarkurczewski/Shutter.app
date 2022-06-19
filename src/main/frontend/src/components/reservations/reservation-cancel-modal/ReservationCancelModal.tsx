import React, { useEffect } from "react";
import { Modal } from "components/shared";
import { useAppDispatch } from "redux/hooks";
import { useTranslation } from "react-i18next";
import { push, ToastTypes } from "redux/slices/toastSlice";
import { Toast } from "types";
import { useCancelReservationMutation } from "redux/service/usersManagementService";

interface Props {
    isOpen: boolean;
    setIsOpen: (value: boolean) => void;
    reservationId: number;
    onSubmit: () => void;
}

export const ReservationCancelModal: React.FC<Props> = ({
                                                            isOpen,
                                                            setIsOpen,
                                                            reservationId,
                                                            onSubmit,
                                                        }) => {
    const { t } = useTranslation();
    const dispatch = useAppDispatch();

    const [cancelReservationMutation, cancelReservationMutationState] =
        useCancelReservationMutation();

    const successToast: Toast = {
        type: ToastTypes.SUCCESS,
        text: t("user_reservations_page.cancel_modal.success"),
    };

    const errorToast: Toast = {
        type: ToastTypes.ERROR,
        text: t("user_reservations_page.cancel_modal.error"),
    };

    useEffect(() => {
        if (cancelReservationMutationState.isSuccess) {
            dispatch(push(successToast));
            onSubmit();
        }
        if (cancelReservationMutationState.isError) {
            dispatch(push(errorToast));
        }
    }, [cancelReservationMutationState]);

    return (
        <Modal
            title={t("user_reservations_page.cancel_modal.title")}
            type="confirm"
            isOpen={isOpen}
            onCancel={() => setIsOpen(false)}
            onSubmit={() => {
                cancelReservationMutation(reservationId);
                setIsOpen(false);
            }}
            submitText={t("user_reservations_page.cancel_modal.submit")}
        >
            <p>{t("user_reservations_page.cancel_modal.description")}</p>
        </Modal>
    );
};