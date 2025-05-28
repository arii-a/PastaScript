package DFA;

import java.util.*;

import DFA.DFADynamic.Condition;
import DFA.DFADynamic.State;
import DFA.DFADynamic.CharCondition;
import DFA.DFADynamic.Whitespace;
import DFA.DFADynamic.Literal;
public class DFA {

    private State currentState = State.START;
    private final Map<State, List<Transition>> transitions = new HashMap<>();
    private final Set<State> acceptStates = new HashSet<>();

    public Set<DFADynamic.State> getAcceptStates() {
        return acceptStates;
    }

    public Map<DFADynamic.State, List<Transition>> getTransitions() {
        return transitions;
    }

    public List<Transition> getTransitions(DFADynamic.State estado) {
        return transitions.getOrDefault(estado, new ArrayList<>());
    }

    public class Transition {
        public Condition condition;
        public State nextState;
        public int consume;
        public String tokenType;

        public Transition(Condition condition, State nextState, int consume, String tokenType) {
            this.condition = condition;
            this.nextState = nextState;
            this.consume = consume;
            this.tokenType = tokenType;
        }
    }

    public void addTransition(State from, Condition cond, State to, int consume, String tokenType) {
        transitions.computeIfAbsent(from, k -> new ArrayList<>()).add(new Transition(cond, to, consume, tokenType));
    }

    public void setAccept(State state) {
        acceptStates.add(state);
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public DFADynamic.State getCurrentState() {
        return currentState;
    }

    public boolean isAcceptState() {
        return acceptStates.contains(currentState);
    }

    public DFA construirDFA() {
        DFA dfa = new DFA();
        Whitespace space = new Whitespace();

        // KEY WORDS
        dfa.addTransition(State.START, new Literal("pasta "), State.PASTA, 6, "pasta");
        dfa.addTransition(State.PASTA, space, State.PASTA, 1, null);

        dfa.addTransition(State.PASTA, new CharCondition(Character::isLetter), State.VAR_NAME_PASTA, 1, "ID");
        dfa.addTransition(State.VAR_NAME_PASTA, new CharCondition(Character::isLetterOrDigit), State.VAR_NAME_PASTA, 1, null);
        dfa.addTransition(State.VAR_NAME_PASTA, space, State.VAR_NAME_PASTA_END, 1, null);
        dfa.addTransition(State.VAR_NAME_PASTA_END, space, State.VAR_NAME_PASTA_END, 1, null);

        dfa.addTransition(State.VAR_NAME_PASTA_END, new Literal("="), State.ASSIGN_PASTA, 1, "igual");

        dfa.addTransition(State.ASSIGN_PASTA, space, State.ASSIGN_PASTA, 1, null);

        dfa.addTransition(State.ASSIGN_PASTA, new CharCondition(Character::isDigit), State.VALUE_PASTA, 1, "entero");

        dfa.addTransition(State.VALUE_PASTA, new CharCondition((Character::isDigit)), State.VALUE_PASTA, 1, null);
        dfa.addTransition(State.VALUE_PASTA, new Literal("g"), State.UNIT_PASTA, 1, "unidad");
        dfa.addTransition(State.UNIT_PASTA, new Literal("~"), State.PASTA_ACCEPT, 1, "end_line");
        dfa.setAccept(State.PASTA_ACCEPT);

        // ----- SAUCE -----
        dfa.addTransition(State.START, new Literal("sauce "), State.SAUCE, 6, "sauce");
        dfa.addTransition(State.SAUCE, space, State.SAUCE, 1, null);

        dfa.addTransition(State.SAUCE, new CharCondition(Character::isLetter), State.VAR_NAME_SAUCE, 1, "ID");
        dfa.addTransition(State.VAR_NAME_SAUCE, new CharCondition((Character::isLetterOrDigit)), State.VAR_NAME_SAUCE, 1, null);
        dfa.addTransition(State.VAR_NAME_SAUCE, space, State.VAR_NAME_SAUCE_END, 1, null);
        dfa.addTransition(State.VAR_NAME_SAUCE_END, space, State.VAR_NAME_SAUCE_END, 1, null);

        dfa.addTransition(State.VAR_NAME_SAUCE_END, new Literal("="), State.ASSIGN_SAUCE, 1, "igual");
        dfa.addTransition(State.ASSIGN_SAUCE, space, State.ASSIGN_SAUCE, 1, null);

        dfa.addTransition(State.ASSIGN_SAUCE, new Literal("\""), State.SAUCE_OPEN_MARKS, 1, "comilla");
        dfa.addTransition(State.SAUCE_OPEN_MARKS, new CharCondition(c -> c != '"'), State.SAUCE_STRING, 1, "string");
        dfa.addTransition(State.SAUCE_STRING, new CharCondition(c -> c != '"'), State.SAUCE_STRING, 1, null);
        dfa.addTransition(State.SAUCE_STRING, new Literal("\""), State.END_STRING_SAUCE, 1, "comilla");
        dfa.addTransition(State.SAUCE_OPEN_MARKS, new Literal("\""), State.END_STRING_SAUCE, 1, "comilla");

        dfa.addTransition(State.END_STRING_SAUCE, new Literal("~"), State.SAUCE_ACCEPT, 1, "end_line");
        dfa.setAccept(State.SAUCE_ACCEPT);

        // ----- BASIL -----
        dfa.addTransition(State.START, new Literal("basil "), State.BASIL, 6, "basil");
        dfa.addTransition(State.BASIL, space, State.BASIL, 1, null);

        dfa.addTransition(State.BASIL, new CharCondition(Character::isLetter), State.VAR_NAME_BASIL, 1, "ID");
        dfa.addTransition(State.VAR_NAME_BASIL, new CharCondition(Character::isLetterOrDigit), State.VAR_NAME_BASIL, 1, null);
        dfa.addTransition(State.VAR_NAME_BASIL, space, State.VAR_NAME_BASIL_END, 1, null);
        dfa.addTransition(State.VAR_NAME_BASIL_END, space, State.VAR_NAME_BASIL_END, 1, null);

        dfa.addTransition(State.VAR_NAME_BASIL_END, new Literal("="), State.ASIGN_BASIL, 1, "igual");
        dfa.addTransition(State.ASIGN_BASIL, space, State.ASIGN_BASIL, 1, null);

        dfa.addTransition(State.ASIGN_BASIL, new Literal("cooked"), State.COOKED_BASIL, 6, "estado_true");
        dfa.addTransition(State.ASIGN_BASIL, new Literal("burned"), State.BURNED_BASIL, 6, "estado_false");
        dfa.addTransition(State.COOKED_BASIL, new Literal("~"), State.BASIL_ACCEPT, 1, "end_line");
        dfa.addTransition(State.BURNED_BASIL, new Literal("~"), State.BASIL_ACCEPT, 1, "end_line");
        dfa.setAccept(State.BASIL_ACCEPT);

        // ----- FUNCTION -----
        dfa.addTransition(State.START, new Literal("recipe "), State.RECIPE, 7, "recipe");
        dfa.addTransition(State.RECIPE, space, State.RECIPE, 1, null);

        dfa.addTransition(State.RECIPE, new CharCondition(Character::isLetter), State.FUNC_NAME, 1, "ID");
        dfa.addTransition(State.FUNC_NAME, new CharCondition(Character::isLetterOrDigit), State.FUNC_NAME, 1, null);
        dfa.addTransition(State.FUNC_NAME, space, State.FUNC_NAME_END, 1, null);
        dfa.addTransition(State.FUNC_NAME_END, space, State.FUNC_NAME_END, 1, null);

        dfa.addTransition(State.FUNC_NAME, new Literal("("), State.FUNC_PAREN_OPEN, 1, "parentesis_apertura");
        dfa.addTransition(State.FUNC_NAME_END, new Literal("("), State.FUNC_PAREN_OPEN, 1, "parentesis_apertura");
        dfa.addTransition(State.FUNC_PAREN_OPEN, space, State.FUNC_PAREN_OPEN, 1, null);

        dfa.addTransition(State.FUNC_PAREN_OPEN, new Literal("pasta "), State.FUNC_ARGS, 6, "pasta");
        dfa.addTransition(State.FUNC_PAREN_OPEN, new Literal("sauce "), State.FUNC_ARGS, 6, "sauce");
        dfa.addTransition(State.FUNC_PAREN_OPEN, new Literal("basil "), State.FUNC_ARGS, 6, "basil");
        dfa.addTransition(State.FUNC_ARGS, space, State.FUNC_ARGS, 1, null);

        dfa.addTransition(State.FUNC_ARGS, new CharCondition(Character::isLetter), State.FUNC_ARGS_NAME, 1, "ID");
        dfa.addTransition(State.FUNC_ARGS_NAME, new CharCondition(Character::isLetterOrDigit), State.FUNC_ARGS_NAME, 1, null);
        dfa.addTransition(State.FUNC_ARGS_NAME, space, State.FUNC_ARGS_NAME_END, 1, null);

        dfa.addTransition(State.FUNC_ARGS_NAME_END, new Literal(")"), State.FUNC_PAREN_CLOSE, 1, "parentesis_cierre");
        dfa.addTransition(State.FUNC_ARGS_NAME, new Literal(")"), State.FUNC_PAREN_CLOSE, 1, "parentesis_cierre");
        dfa.addTransition(State.FUNC_PAREN_CLOSE, space, State.FUNC_PAREN_CLOSE, 1, null);

        dfa.addTransition(State.FUNC_PAREN_CLOSE, new Literal("{"), State.FUNC_BODY_OPEN, 1, "llave_apertura");
        dfa.addTransition(State.FUNC_BODY_OPEN, space, State.FUNC_BODY_OPEN, 1, null);

        dfa.addTransition(State.FUNC_BODY_OPEN, new Literal("pasta "), State.PASTA, 6, "pasta");
        dfa.addTransition(State.FUNC_BODY_OPEN, new Literal("sauce "), State.SAUCE, 6, "sauce");
        dfa.addTransition(State.FUNC_BODY_OPEN, new Literal("basil "), State.BASIL, 6, "basil");

        dfa.addTransition(State.PASTA_ACCEPT, new Literal("}"), State.FUNC_ACCEPT, 1, "llave_cierre");
        dfa.addTransition(State.SAUCE_ACCEPT, new Literal("}"), State.FUNC_ACCEPT, 1, "llave_cierre");
        dfa.addTransition(State.BASIL_ACCEPT, new Literal("}"), State.FUNC_ACCEPT, 1, "llave_cierre");

        dfa.addTransition(State.PASTA_ACCEPT, space, State.FUNC_BODY_OPEN, 1, null);
        dfa.addTransition(State.SAUCE_ACCEPT, space, State.FUNC_BODY_OPEN, 1, null);
        dfa.addTransition(State.BASIL_ACCEPT, space, State.FUNC_BODY_OPEN, 1, null);

        dfa.addTransition(State.FUNC_BODY_OPEN, space, State.FUNC_BODY_OPEN, 1, null);
        dfa.addTransition(State.FUNC_BODY_OPEN, new Literal("}"), State.FUNC_ACCEPT, 1, "llave_cierre");

        dfa.setAccept(State.FUNC_ACCEPT);

        return dfa;
    }
}

