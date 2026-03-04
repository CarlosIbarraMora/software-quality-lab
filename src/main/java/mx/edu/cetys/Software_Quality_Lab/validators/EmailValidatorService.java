package mx.edu.cetys.Software_Quality_Lab.validators;

public class EmailValidatorService {

    public boolean isValid(String email) {

        if (email == null || email.isEmpty()) return false;


        if (!validSeparation(email)) return false;

        String[] parts = email.split("#");
        if(parts[0].isEmpty() || parts[1].isEmpty()) return false;
        String user = parts[0];
        String domain = parts[1];

        if (!validContains4(email)) return false;
        if (!validEmailLength(email)) return false;
        if (!validDomainLength(domain)) return false;
        if (!validUserCharacters(user)) return false;
        if (!validDomainCharacters(domain)) return false;
        if (!validDiphthong(email)) return false;

        return true;
    }

    //checks if email length no longer than 47
    private boolean validEmailLength(String email) {
        return email.length() <= 47 ;
    }

    //checks if domain length no longer than 5
    private boolean validDomainLength(String domain) {
        String[] providerDomain = domain.split("\\.");
        if (providerDomain.length < 2) return false;
        return providerDomain[1].length() <= 5;
    }

    //Checks if user part of email only contains: a-z 0-9.+_-
    private boolean validUserCharacters(String user) {
        return user.matches("^[a-z0-9.+_-]+$");
    }

    //Checks if domain part of email only contains: a-z, 0-9.
    private boolean validDomainCharacters(String domain) {
        return domain.matches("^[a-z0-9.]+$");
    }

    //Estan bien locas las expresiones regulares
    //Checks if in the whole email it doesn't contain one vowel after another
    private boolean validDiphthong(String user) {
        return !user.matches(".*[aeiou]{2}.*");
    }

    //checks if email is separated by #
    private boolean validSeparation(String email) {
        String[] temp = email.split("#");
        return temp.length == 2;
    }

    //checks if 4 is present in the email
    private boolean validContains4(String email) {
        return email.contains("4");
    }
}