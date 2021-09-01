package fr.sgo.mastermindserver.checker;

class Pair {
    private final Pawn solution;
    private final Pawn proposition;
    private boolean missPlaced = false;

    Pair(Pawn solution, Pawn proposition) {
        this.solution = solution;
        this.proposition = proposition;
    }

    public Result.State getState() {
        if (solution == proposition) {
            return Result.State.CORRECT;
        } else {
            return missPlaced ? Result.State.MISS_PLACED : Result.State.INCORRECT;
        }
    }

    public boolean isCorrect() {
        return getState() == Result.State.CORRECT;
    }

    public Pawn getProposition() {
        return proposition;
    }

    public boolean isMissPlaced() {
        return getState() == Result.State.MISS_PLACED;
    }

    public void missPlaced() {
        this.missPlaced = true;
    }
}
