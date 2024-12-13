module com.uhl.playerdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.uhl.playerdb to javafx.fxml;
    exports com.uhl.playerdb;
    exports com.uhl.playerdb.controller;
    opens com.uhl.playerdb.controller to javafx.fxml;

    exports com.uhl.playerdb.Networking;
}