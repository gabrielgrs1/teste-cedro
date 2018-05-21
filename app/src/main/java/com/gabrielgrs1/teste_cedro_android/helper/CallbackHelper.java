package com.gabrielgrs1.teste_cedro_android.helper;

public class CallbackHelper {

    public static String getErrorMessage(String errorString) {
        String[] errorArray = errorString.split(",");
        String error = "Erro não capturado";
        boolean errorBool = false;


        if (errorArray.length >= 1) {
            errorArray = errorArray[1].split(":");
            error = errorArray[1].replace("\"", "").replace("}", "");

            if (errorArray.length == 3) {
                errorBool = true;
            }
        }
        return translateError(error, errorBool);
    }

    /**
     * Translate error returned by api
     *
     * @param error     error string returned by api
     * @param errorBool true case means it is a registration error
     * @return translated error
     */
    private static String translateError(String error, boolean errorBool) {
        switch (error) {
            case "Invalid e-mail or password":
                return "Email e/ou senha inválida!";
            case "Invalid e-mail":
                return "Email inválido!";
            case "Missing required parameters":
                return "Informe seu email e senha!";
            case "Duplicate key for property email":
                return "Email já cadastrado!";
        }

        if (errorBool || "Invalid password".equals(error)) {
            return "A senha deve corresponder a todos os requisitos acima!";
        }

        return error;
    }
}
