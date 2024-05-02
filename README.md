# Java PasswordManager

Java  PasswordManager is  a command-line  command,
that manages passwords.

> **Disclaimer:** This program currently (as of version 0.1.0) doesn't use encryption to store the passwords. Then, it is NOT recommended to use the program where security is a primary concern. By using this program, you acknowledge the nature of storing the passwords. It is then, your responsibility to not store any sensitive information.

## Installation & Usage

[Download](https://github.com/rofe33/java-password-manager/releases/download/v0.1.0/java-password-manager-0.1.0.jar) `java-password-manager-0.1.0.jar` from the [release page](https://github.com/rofe33/java-password-manager/releases/tag/v0.1.0).

Then run the jar file using java:

> Note: It is advisable to have java version 17.

```sh
java -jar java-password-manager-0.1.0.jar
```

After  running  the  jar  file, you  will  have  a
command-line prompt.  Type `help` to know  all the
available commands and their use case.

By  default   `SQLite`  is   used  to   store  the
passwords, however you can change it to use `CSV`.

### Example

```txt
Command (type 'help' for help): manager
Manager's Command (type 'help' for help): add
Enter website: github.com
Enter username: rofe33
Generating a Password...
Please enter your preferences: (true/false) (Default: true)
Use lower case characters?
Use upper case characters?
Use digit characters?
Use punctuation characters? false
Enter the length of the password (Default: 16): 32
Manager's Command (type 'help' for help): list
Index: 1, Website: github.com, Username: rofe33, Password: 4eEBD9INiskfFxfot2pZuSW045cB0muX
Manager's Command (type 'help' for help): exit
```
