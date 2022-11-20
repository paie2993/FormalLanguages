package org.compilers.finite_automaton.finite_automaton.domain.vocabulary.state;

public final class StateImpl implements State {

    private final String state;

    public StateImpl(final String state) {
        this.state = state;
    }

    @Override
    public String representation() {
        return state;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof StateImpl oState)) {
            return false;
        }
        return oState.state.equals(this.state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public String toString() {
        return representation();
    }
}
