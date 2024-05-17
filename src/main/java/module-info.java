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

    opens org.solideinc.solidequerydistributor to javafx.fxml;
    exports org.solideinc.solidequerydistributor;
    exports org.solideinc.solidequerydistributor.Controllers;
    exports org.solideinc.solidequerydistributor.Classes;
    opens org.solideinc.solidequerydistributor.Controllers to javafx.fxml;
}