package Primer_parcial;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFAMarte {
    private int currentState;
    private Set<Integer> acceptingStates;
    private Map<Integer, Map<Character, Integer>> transitions;

    public DFAMarte() {
        currentState = 0;
        acceptingStates = new HashSet<>();
        transitions = new HashMap<>();

        initializeDFA();
    }

    private void initializeDFA() {

        acceptingStates.add(1);
        acceptingStates.add(2);

        acceptingStates.add(3);


        for (int i = 0; i <= 30; i++) {
            transitions.put(i, new HashMap<>());
            transitions.get(i).put('0', -1);
            transitions.get(i).put('1', -1);
        }


        // Estado 0: q0_start (Inicio)
        transitions.get(0).put('0', 1);  // 0
        transitions.get(0).put('1', 2);  // 1

        transitions.get(1).put('0', 1);
        transitions.get(1).put('1', 2);

        transitions.get(2).put('0', 1);
        transitions.get(2).put('1', 3);

        transitions.get(3).put('0', 1);
    }

    /**
     * Procesa un carácter de entrada y actualiza el estado actual del DFA.
     * Si la transición no existe, se va al estado sumidero (-1).
     * @param inputChar El carácter a procesar ('0' o '1').
     */
    public void processChar(char inputChar) {
        if (currentState == -1) { // Si ya estamos en el estado sumidero, nos quedamos allí
            return;
        }
        Map<Character, Integer> stateTransitions = transitions.get(currentState);
        if (stateTransitions != null && stateTransitions.containsKey(inputChar)) {
            currentState = stateTransitions.get(inputChar);
        } else {
            currentState = -1; // Ir al estado sumidero si no hay una transición definida
        }
    }

    /**
     * Resetea el DFA a su estado inicial.
     */
    public void reset() {
        currentState = 0;
    }

    /**
     * Verifica si la cadena procesada es aceptada por el DFA (es un palíndromo válido
     * y de la longitud correcta).
     * @return true si el estado actual es un estado de aceptación, false en caso contrario.
     */
    public boolean isAccepted() {
        return acceptingStates.contains(currentState);
    }

    public static void main(String[] args) {
        DFAMarte dfa = new DFAMarte();

        String[] testStrings = {
                //correctos
                "0", "1", "10", "01", "101", "1101", "10101010",

                //inválidos
                "1111", "0111010", "0111", "1110"
        };

        System.out.println("--- Probando Lenguaje marte binarios ---");
        for (String s : testStrings) {
            dfa.reset(); // Resetear el DFA para cada nueva cadena
            for (char c : s.toCharArray()) {
                dfa.processChar(c);
            }

            boolean accepted = dfa.isAccepted();
            System.out.printf("Cadena \"%s\": %s%n", s, accepted ? "Aceptada" : "Rechazada");
        }
    }

}
