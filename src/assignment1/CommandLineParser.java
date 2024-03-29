package assignment1;

import assignment1.exceptions.InvalidCommandLineArgument;
import assignment1.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// Used to parse the commandline arguments
class CommandLineParser {

    private  void validateNumberOfArguments(ArrayList<String> args) {
        if (args.size() < 4)
            throw new InvalidCommandLineArgument("At least 4 commandline arguments needed and -name and -type options are must.");
    }

    private  void validateCompulsoryArguments(ArrayList<String> args) {
//         -name should be the first arg and -type option must be present
        if (args.indexOf("-" + Strings.name) != 0)
            throw new InvalidCommandLineArgument("-name option must be the first option !");
        if (args.indexOf("-" + Strings.type) < 0) throw new InvalidCommandLineArgument("-type option is compulsory !");
    }

    private  void validateEachOptionHasAValue(ArrayList<String> args) {
//        An option should have a corresponding value as its next argument
        for (Iterator<String> s = args.iterator(); s.hasNext(); ) {
            if (s.next().startsWith("-") && (s.hasNext() && s.next().startsWith("-"))) {
                throw new InvalidCommandLineArgument("The value for options must not start with a dash '-' ");
            }
        }
    }

    private  void validateTypeOption(ArrayList<String> args) {
//        allow only some values for type
        String type = args.get(args.indexOf("-" + Strings.type) + 1);
        switch (type) {
            case "raw":
            case "manufactured":
            case "imported":
                break;
            default:
                throw new InvalidCommandLineArgument("Invalid 'Item Type' specified !");
        }
    }

    private  void validateOptionHasCorrectDataType(ArrayList<String> args) {
        try {
            if (args.indexOf("-" + Strings.quantity) >= 0)
                Integer.parseInt(args.get(args.indexOf("-" + Strings.quantity) + 1));
            if (args.indexOf("-" + Strings.price) >= 0)
                Double.parseDouble(args.get(args.indexOf("-" + Strings.price) + 1));
        } catch (NumberFormatException e) {
            throw new InvalidCommandLineArgument("Invalid input ! Make sure that item quantity is integer and price is a number.");
        }
    }
    private  void validate(ArrayList<String> args) throws InvalidCommandLineArgument {
        validateNumberOfArguments(args);
        validateCompulsoryArguments(args);
        validateEachOptionHasAValue(args);
        validateTypeOption(args);
        validateOptionHasCorrectDataType(args);
    }


    // Returns an ItemDetail based on the arguments given here after validating their values
     ItemDetail getItemDetail(String[] args) throws InvalidCommandLineArgument {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        validate(argsList);

        String type = argsList.get(argsList.indexOf("-" + Strings.type) + 1);
        String name = argsList.get(argsList.indexOf("-" + Strings.name) + 1);
        Double price = null;
        Integer quantity = null;

        if (argsList.indexOf("-" + Strings.price) >= 0) {
            price = Double.parseDouble(argsList.get(argsList.indexOf("-" + Strings.price) + 1));
        }
        if (argsList.indexOf("-" + Strings.quantity) >= 0) {
            quantity = Integer.parseInt(argsList.get(argsList.indexOf("-" + Strings.quantity) + 1));
        }
        if (quantity == null) quantity = 1; //default quantity

        switch (type) {
            case Strings.raw:
                return new ItemDetail(new RawItem(name, price), quantity);
            case Strings.manufactured:
                return new ItemDetail(new ManufacturedItem(name, price), quantity);
            case Strings.imported:
                return new ItemDetail(new ImportedItem(name, price), quantity);
            default:
                return new ItemDetail(new Item("InvalidItem", (double) 0), 0);
        }
    }
}
