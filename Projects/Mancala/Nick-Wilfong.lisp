;;;Nick Wilfong Project 4, Mancala
;;;CS480 G00714607



;;;; Here is a description of the stuff that would go into your
;;;; file.

;;;; The first thing in your function is a package declaration.
;;;; You should name your package something beginning with a colon
;;;; followed by your full name with hyphens for spaces.
;;;; I named my package :sean-luke .  Keep in mind that the name
;;;; is CASE-INSENSITIVE.  The only symbol you should export is the
;;;; symbol COMPUTER-MAKE-MOVE, which should be the name of your top-
;;;; level computer move function.  Name your file the same
;;;; name as your package declaration, minus the colon.  For example,
;;;; my file is named "sean-luke.lisp"
                                        
(defpackage :nick-wilfong
  (:use :common-lisp-user :common-lisp)                                       
  (:export computer-make-move))
(in-package :nick-wilfong)


;;;; Once you've done this, you need to write your code.  Here
;;;; is a rough sketch of three functions you will find handy.
;;;; You don't need to implement them like this, except for the
;;;; COMPUTER-MAKE-MOVE function. You can write your code in
;;;; any fashion you like in this file, so long as you do a
;;;; proper alpha-beta heuristic search and your evaluation
;;;; function is stronger than just comparing the differences in
;;;; the mancalas.

(defun alpha-beta (state current-depth max-depth
                      max-player expand terminal evaluate
                      alpha beta)
  "Does alpha-beta search.  Note that there is the addition of
a variable called MAX-PLAYER rather than a function which specifies
if it's max's turn.  It's just more convenient in this system.
The MAX-PLAYER variable is set to either *player-1*
or to *player-2* and indicates if *player-1* or *player-2* should
be considered 'max' (the other player is then considered to be
'min')"


  (let ((s state)
        (d current-depth)
        (max-d max-depth)
        (a alpha)
        (b beta)
        (childs (funcall expand state)))
    
    
    (cond
      ;;check if the game is over or we've passed the max-depth.
      ((or (funcall terminal s) (>= d max-d))
       (funcall evaluate s max-player))

      ;;if the player going is max player
      ((equalp max-player (state-turn s))
       (dolist (c childs a)
         ;;set alpha to the max between the current alpha and the alphas found from the child states
         (setf a (max a (alpha-beta c (1+ d) max-d max-player expand terminal evaluate a b)))
         (if (>= a b)
             (return b))))

      ;;if the above is not true then it must be the min players turn
      ((not (equalp max-player (state-turn s)))
       (dolist (c childs b)
         ;;set beta to the min between the current beta and the betas found from the child states
         (setf b (min b (alpha-beta c (1+ d) max-d max-player expand terminal evaluate a b)))
         (if (>= a b)
             (return a)))))))



(defun end-pit (st pit)
  "Helper fuction for evaluate.  Given a starting pit, returns the INDEX of the pit reached
upon 'sowing' all the stones in the starting pit."

  (let ((new-board (copy-seq (state-board st)))
        (player-turn (state-turn st))
        cur-pos
        skip-mancala)

    ;; determine the pit indices
    (if (eq player-turn *player-1*)
        ;; pits start at i=0, skipped mancala starts at *num-pits* * 2 + 1,
        (setf cur-pos (+ pit (left-pit *player-1*))
              skip-mancala (mancala-pit *player-2*))
        ;; pits start at i=*num-pits*+1, skipped mancala starts at *num-pits*,
        (setf cur-pos (+ pit (left-pit *player-2*))
              skip-mancala (mancala-pit *player-1*)))

    ;; loop through sowing process
    (let ((numstones (svref new-board cur-pos))
          (board-length (board-size)))
      (unless (= numstones 0)
        ;; sow
        (setf (svref new-board cur-pos) 0)
        (loop for stones downfrom numstones above 0
              do
              (incf cur-pos)
              (when (= cur-pos skip-mancala) (incf cur-pos))
              (when (= cur-pos board-length) (setf cur-pos 0))
              (incf (svref new-board cur-pos)))))


    ;;returns current position, where all stones are gone
    cur-pos))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;NEEDS MORE;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defun side-total (state player)
  "Another helper function, returns the total number of stones on a given side of 
the board"
  (let ((total 0))
    (dotimes (i *num-pits* total)
      (incf total (svref (state-board state) i)))))  


(defun evaluate (state max-player)
  "Evaluates the game situation for MAX-PLAYER.
Returns the value of STATE for MAX-PLAYER (who
is either *player-1* or *player-2*).  This should
be a value ranging from *min-wins* to *max-wins*."

  (let ((heuristic-score (score state max-player))
        (starting-pit (left-pit (state-turn state)))
        (mancala (mancala-pit (state-turn state)))
        (stones 0))
    
    ;;if the game is over we just need to see if the max-player has more pieces than his opponent
    (if (game-overp state)
        (if (> (score state max-player) 0)
            (setf heuristic-score *max-wins*)
            (setf heuristic-score *min-wins*))

        ;;if the game is not over
        (progn
          ;;if a move will result in another move (ends in a mancala)
          (if *go-again-rule*
              (dotimes (x *num-pits*)                                  
                (if (= (end-pit state x) mancala)
                    
                    ;;for maxs turn add, mins turn subtract
                    (if (= max-player (state-turn state))
                        (setf heuristic-score (+ heuristic-score 2))
                        (setf heuristic-score (- heuristic-score 2))))))
          
          ;;if a move will result in a 'big-win'
          (if *big-win-rule*
              (dotimes (x *num-pits*)
                
                ;;the requirement here is a pit move will end up in an empty pit on their side (not a mancala) and that pit has stones across from it
                (if (and
                     ;;end-pit is a spot the player owns
                     (and (>= (end-pit state x) starting-pit) (<= (+ starting-pit (1- *num-pits*))))
                     ;;end-pit is empty
                     (= (svref (state-board state) (end-pit state x)) 1)
                     ;;the end-pit has stones across
                     (> (svref (state-board state) (pit-opposite (end-pit state x))) 0))

                    
                    ;;add for max's turn, subtract for mins
                    (progn (setf stones (svref (state-board state) (pit-opposite (end-pit state x))))
                           (if (= max-player (state-turn state))
                               (setf heuristic-score (+ heuristic-score stones))
                               (setf heuristic-score (- heuristic-score stones)))) )))

          ;;;;below will work even if big-win and go-again are false;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;NEEDS MORE;;;;;;;;;;;;;;;;;;;;;;;;;;;;

          ;;if youre already winning, try to clear your side of the board as fast as possible.
          ;;not as effective will only be weighted as one
          (setf stones (side-total state ()))
          (if (> (score state max-player) 0)
              (progn (dotimes (x *num-pits*)
                       (setf stones (+ stones (svref (state-board state) s))))

                     (dotimes (x *num-pits*)
                       )))))

    

    ;;return the heuristic score
    heuristic-score))

(defun computer-make-move (state max-depth)
  "Given a state, makes a move and returns the new state.
If there is no move to make (end of game) returns nil.
Each time this function calls the top-level
alpha-beta function to search for the quality of a state,
computer-make-move should print out the state (using PRINT,
not PRINT-STATE) that is being searched.
Only search up to max-depth.  The computer should assume
that he is the player who's turn it is right now in STATE"


  (let ((max-d max-depth)
        (best-state nil)
        (best-score *min-wins*)
        (childs (moves state))
        (cur-score))

    ;;check if game is over
    (if (game-overp state)
        (return-from computer-make-move nil))
    
    ;;otherwise
    (dolist (c childs)
      (setf cur-score (alpha-beta c 0 max-d (state-turn state) #'moves #'game-overp #'evaluate *min-wins* *max-wins*))
      ;;print stuff
      (print c)
      
      (if (> cur-score best-score)
          (progn (setf best-state c)
                 (setf best-score cur-score))))

    ;;return the best move
    best-state))










;;;; In comments your file, you put your project notes.

;;;;NOTES
;;;;
;;;;First I implemented the alpha-beta pruning algorithm that we learned in class.  It had to be modified
;;;;slightly due to the fact that we are using a 'max-player' parameter rather than a 'is-max-turn' predicate
;;;;function. The 'make-move' function was next, following the algorithm in the notes for a general game playing
;;;;agent.  Once these two frameworks were in place it was time to develop the heuristic.
;;;;Using the number of stones in the players mancala as a starting point:
;;;;
;;;;                                                   Since the leftover stones are actually not added to an opponents (bug?)
;;;;                                                   mancala when the game ends, any state that ends up with less pieces
;;;;                                                   on its side than the opponent (while your mancala is bigger) gets a better score,
;;;;                                                   you are closer to ending the game as a winner
;;;;
;;;;                                                   if big-win is true, open slots on your side with stones in the opponents side
;;;;                                                   increase the score.
;;;;
;;;;                                                   if go-again is true, states with moves that will lead to another turn get an increase
;;;;
;;;;                                                   in either case, if there is a slot with stones in it, but it is too far away from the
;;;;                                                   mancala to score in one move (num stones < distance from mancala) that space is worth 0
;;;;                                                   and other moves should be prioritized before it
;;;;
;;;;This approach is a little naive in assuming that all the above factors have equal effect on the qualitiy of a move, but still is an
;;;;effective basic approach.  This could probably be improved by making the influcene of each factor a weight ratio instead of simply binary 1 or 0,
;;;;after it is determined which have a more effect than others.

;;;; The last thing in your file should be this line (uncommented
;;;; of course).

(in-package :cl-user)
