public class Test {

    public static boolean emailValidator(String email){

        if(email.endsWith(".com")&& email.contains("@")){
            return true;
        }

        return  false;
    }

    public static void main(String[] args) {

    }
}
