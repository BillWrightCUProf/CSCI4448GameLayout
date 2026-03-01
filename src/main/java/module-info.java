module CSCD454GameLayout.main {
    requires java.desktop;  // Required for java.awt and javax.swing
    requires org.slf4j;

    requires org.jgrapht.core;

    exports csci.ooad.layout.intf;
    exports csci.ooad.layout.example;
    exports csci.ooad.layout;
}