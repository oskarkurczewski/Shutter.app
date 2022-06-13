import React, { FC } from "react";
import { MdInfo } from "react-icons/md";
import { Toast } from ".";

interface Props {
   text: string;
   id: number;
}

export const ToastInfo: FC<Props> = ({ text, id }) => {
   return <Toast id={id} icon={<MdInfo />} text={text} />;
};
