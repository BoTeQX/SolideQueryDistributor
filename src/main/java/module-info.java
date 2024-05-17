module org.solideinc.solidequerydistributor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires ollama4j;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;

    opens org.solideinc.solidequerydistributor to javafx.fxml;
    opens org.solideinc.clas to com.fasterxml.jackson.databind;
    exports org.solideinc.solidequerydistributor;
    exports org.solideinc.solidequerydistributor.Controllers;
    opens org.solideinc.solidequerydistributor.Controllers to javafx.fxml;
    exports org.solideinc.solidequerydistributor.Utils;
    opens org.solideinc.solidequerydistributor.Utils to javafx.fxml;
}