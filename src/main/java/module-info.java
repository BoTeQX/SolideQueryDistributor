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
    requires bcrypt;
    requires java.desktop;

    opens org.solideinc.solidequerydistributor to javafx.fxml;
    opens org.solideinc.solidequerydistributor.Classes to com.fasterxml.jackson.databind;
    exports org.solideinc.solidequerydistributor;
    exports org.solideinc.solidequerydistributor.Controllers;
    exports org.solideinc.solidequerydistributor.Classes;
    opens org.solideinc.solidequerydistributor.Controllers to javafx.fxml;
    exports org.solideinc.solidequerydistributor.Util;
    opens org.solideinc.solidequerydistributor.Util to javafx.fxml;
}