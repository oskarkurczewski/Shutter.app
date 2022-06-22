import { Button } from "components/shared";
import styles from "./LikeButton.module.scss";
import React, { useEffect } from "react";
import Animation from "assets/animations/like_boom.json";
import { Player } from "@lottiefiles/react-lottie-player";

interface Props {
   liked: boolean;
   likeCount: number;
   showAnimation: boolean;
   setShowAnimation: (value: boolean) => void;
   onLike: () => void;
   onUnlike: () => void;
}

export const LikeButton: React.FC<Props> = ({
   likeCount,
   liked,
   onLike,
   onUnlike,
   showAnimation,
   setShowAnimation,
}) => {
   // Remove animation after 1s
   useEffect(() => {
      if (showAnimation) {
         setTimeout(() => {
            setShowAnimation(false);
         }, 1000);
      }
   }, [showAnimation]);

   return (
      <div className={styles.wrapper}>
         {showAnimation && <Player autoplay src={Animation} background="transparent" />}
         <Button
            className={`${styles.button_wrapper} ${liked ? styles.liked : ""}`}
            onClick={() => {
               liked ? onUnlike() : onLike();
            }}
            icon="favorite"
         >
            {likeCount?.toString()}
         </Button>
      </div>
   );
};
