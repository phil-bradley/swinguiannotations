# swinguiannotations
Annotations to execute methods on the EDT

The purpose of this library is to allow for methods to be annotated so that they will be executed on the Swing Event Dispatch Thread (EDT) without the use of SwingUtilities or similar.


    @AsyncUiThread
    public void doStuff() {
        // Executes on the EDT, invoked via invokeLater
    }

    @UiThread
    public void doOtherStuff() {
        // Executes on the EDT, invoked via invokeAndWait
    }
