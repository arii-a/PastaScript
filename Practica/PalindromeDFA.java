package Practica;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PalindromeDFA {

    private int currentState;
    private Set<Integer> acceptingStates;
    private Map<Integer, Map<Character, Integer>> transitions;

    public PalindromeDFA() {
        currentState = 0;
        acceptingStates = new HashSet<>();
        transitions = new HashMap<>();

        initializeDFA();
    }

    private void initializeDFA() {

        acceptingStates.add(1);
        acceptingStates.add(2);

        acceptingStates.add(3);
        acceptingStates.add(4);

        acceptingStates.add(7);
        acceptingStates.add(8);
        acceptingStates.add(9);
        acceptingStates.add(10);

        acceptingStates.add(15);
        acceptingStates.add(18);

        acceptingStates.add(25);
        acceptingStates.add(28);
        acceptingStates.add(30);
        acceptingStates.add(31);
        acceptingStates.add(37);
        acceptingStates.add(40);
        acceptingStates.add(42);
        acceptingStates.add(44);
        acceptingStates.add(46);
        acceptingStates.add(47);
        acceptingStates.add(49);
        acceptingStates.add(50);
        acceptingStates.add(52);


        for (int i = 0; i <= 30; i++) {
            transitions.put(i, new HashMap<>());
            transitions.get(i).put('0', -1);
            transitions.get(i).put('1', -1);
        }


        // Estado 0: q0_start (Inicio)
        transitions.get(0).put('0', 1);  // 0
        transitions.get(0).put('1', 2);  // 1

        // --- Longitud 1 (Estados de Aceptación) ---
        transitions.get(1).put('0', 3);  // "0" + "0"
        transitions.get(1).put('1', 5);  // "0" + "1"

        transitions.get(2).put('0', 6);  // "1" + "0"
        transitions.get(2).put('1', 4);  // "1" + "1"

        // --- Longitud 2 (Estados de Aceptación) ---
        transitions.get(3).put('0', 7);  // "00" + "0" ->
        transitions.get(3).put('1', 11); // "00" + "1" ->

        transitions.get(4).put('0', 13); // "11" + "0"
        transitions.get(4).put('1', 10); // "11" + "1"

        // --- Longitud 3 (Estados intermedios y de Aceptación) ---
        transitions.get(5).put('0', 8);  // "01" + "0"
        transitions.get(5).put('1', 12); // "01" + "1"

        transitions.get(6).put('0', 14); // "10" + "0"
        transitions.get(6).put('1', 9);  // "10" + "1"

        transitions.get(7).put('0', 15); // "000" + "0"
        transitions.get(7).put('1', 19); // "000" + "1"

        transitions.get(8).put('0', 20); // "010" + "0"
        transitions.get(8).put('1', 16); // "010" + "1"

        transitions.get(9).put('0', 17); // "101" + "0"
        transitions.get(9).put('1', 21); // "101" + "1"

        transitions.get(10).put('0', 22); // "111" + "0"
        transitions.get(10).put('1', 18); // "111" + "1"

        // --- Longitud 4 (Estados intermedios y de Aceptación) ---
        transitions.get(11).put('0', 23); // "001" + "0"
        transitions.get(11).put('1', 24); // "001" + "1"

        transitions.get(12).put('0', 25); // "011" + "0"
        transitions.get(12).put('1', 26); // "011" + "1"

        transitions.get(13).put('0', 27); // "110" + "0"
        transitions.get(13).put('1', 28); // "100" + "1"

        transitions.get(14).put('0', 29); // "100" + "0"
        transitions.get(14).put('1', 30); // "100" + "1"


        // --- Longitud 5 (Estados de Aceptación) ---
        transitions.get(15).put('0', 47); // "0000" + "0"
        transitions.get(15).put('1', 48); // "0000" + "1"

        transitions.get(16).put('0', 49); // "0101" + "0"
        transitions.get(16).put('1', 50); // "0101" + "1"

        transitions.get(17).put('0', 51); // "1010" + "0"
        transitions.get(17).put('1', 52); // "1010" + "1"

        transitions.get(18).put('0', 49); // "1111" + "0"
        transitions.get(18).put('1', 50); // "1111" + "1"

        transitions.get(23).put('0', 31); // "0010" + "0"
        transitions.get(23).put('1', 32); // "0010" + "1"

        transitions.get(24).put('0', 33); // "0011" + "0"
        transitions.get(24).put('1', 34); // "0011" + "1"

        transitions.get(25).put('0', 35); // "0110" + "0"
        transitions.get(25).put('1', 36); // "0110" + "1"

        transitions.get(26).put('0', 37); // "0111" + "0"
        transitions.get(26).put('1', 38); // "0111" + "1"

        transitions.get(27).put('0', 39); // "1000" + "0"
        transitions.get(27).put('1', 40); // "1000" + "1"

        transitions.get(28).put('0', 41); // "1101" + "0"
        transitions.get(28).put('1', 42); // "1101" + "1"

        transitions.get(29).put('0', 43); // "1000" + "0"
        transitions.get(29).put('1', 44); // "1000" + "1"

        transitions.get(30).put('0', 45); // "1101" + "0"
        transitions.get(30).put('1', 46); // "1101" + "1"

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
        PalindromeDFA dfa = new PalindromeDFA();

        String[] testStrings = {
                "0", "1",           // 1 dígito
                "00", "11",         // 2 dígitos
                "000", "010", "101", "111", // 3 dígitos
                "0000", "0110", "1001", "1111", // 4 dígitos
                "00000", "00100", "01010", "01110", "10001", "10101", "11011", "11111", // 5 dígitos

                // No palíndromos o longitudes incorrectas
                "", "01", "10", "001", "110", "100", "011",
                "1010", "0101", "1110", "0001",
                "10000", "00001", "111111", "100001", "0010" // Longitud incorrecta
        };

        System.out.println("--- Probando Palíndromos Binarios (1 a 5 dígitos) ---");
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
