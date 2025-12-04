module com.ndominkiewicz.dfa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.ndominkiewicz.dfa to javafx.fxml;
    exports com.ndominkiewicz.dfa;
    opens com.ndominkiewicz.dfa.controller to javafx.fxml;
    exports com.ndominkiewicz.dfa.controller;
}