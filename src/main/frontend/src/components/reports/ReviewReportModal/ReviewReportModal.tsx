import { Modal } from "components/shared";
import React from "react";
import styles from "./ReviewReportModal.module.scss";
import { useGetReviewByIdQuery } from "redux/service/reviewService";
import { DateTime } from "luxon";
import { MdThumbUp } from "react-icons/md";
import { HiCamera, HiClock, HiUser } from "react-icons/hi";
import { InfoElement } from "./InfoElement";
import { FaEye, FaEyeSlash } from "react-icons/fa";

interface Props {
   isOpen: boolean;
   onSubmit: () => void;
   reviewId: number;
}

export const ReviewReportModal: React.FC<Props> = ({ isOpen, onSubmit, reviewId }) => {
   const reviewData = useGetReviewByIdQuery(reviewId);
   console.log(reviewData);
   return (
      <Modal
         type="info"
         isOpen={isOpen}
         onSubmit={onSubmit}
         //  title={`Recenzja ${} `}
         title={
            <div className={styles.title}>
               Recenzja {reviewData?.data?.id}{" "}
               {reviewData?.data?.active ? (
                  <FaEye className={styles.eye} />
               ) : (
                  <FaEyeSlash className={styles.eye} />
               )}
            </div>
         }
         className={styles.review_report_modal_wrapper}
      >
         <section>
            <div className={styles.info}>
               <InfoElement icon={<HiUser />}>
                  {reviewData?.data?.reviewerLogin}
               </InfoElement>
               <InfoElement icon={<HiCamera />}>
                  {reviewData?.data?.photographerLogin}
               </InfoElement>
               <InfoElement icon={<MdThumbUp />}>
                  {reviewData?.data?.likeCount}
               </InfoElement>
               <InfoElement icon={<HiClock />}>
                  {DateTime.fromJSDate(new Date(reviewData?.data?.createdAt)).toFormat(
                     "dd.MM.yyyy, HH.mm.ss"
                  )}
               </InfoElement>
            </div>
            <div className={styles.content}>
               <div>Gwiazdki</div>
               <p>
                  Senny ptaki Dobra zegar. Owoce łono niém Szła niéj. Nikt chce piorunowym
                  polu roli wysmukłą zły francuskim źwierząt. Rzeźbiarstwie chleba wkoło
                  replik Roskrzyżował wyciągniętą pieszo kartą nieuszanowanie
                  niedźwiedziem. Też skrzydłami najpierwéj jest Tyle ptak krzyknęła
                  przysłowie Zaś węzłowate robi podniosłem. Smycze Podniesionemi pasach
                  zadać książeczkę nadzwyczajnie Kazał dramatycznych wstąg Parafianowicz
                  pierś Przywołując.
               </p>
            </div>
         </section>
      </Modal>
   );
};
