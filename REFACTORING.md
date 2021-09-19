# Réusinage

Ce document résume les différentes étapes effectuées pour réusiner le code.

1. Ajout de la méthode abstraite `getPayForTwoWeeks` dans la classe `Employee`
    1. Déplacement du code de `createPendingPaycheck` dans les sous-classes
    2. Permet de retirer les `instanceof` dans `CompanyPayroll`
2. Ajout de la méthode abstraite `giveRaise` dans la classe `Employee`
    1. Similaire au point 1.
3. Simplification de `takeHoliday`
    1. Ajout de la méthode `takeHoliday` dans `Employee`, cette méthode permet de vérifier s'il peut prendre `amount`
       jours de congé et de retirer les jours de congés de la banque de l'employée.
    2. Ajout de la méthode abstraite `takePayout` dans `Employee` et implémentation dans les sous-classes en déplaçant
       le code de `CompanyPayroll`
4. Suppression du tableau `isEmployeeTakingHolidays` dans `CompanyPayroll`
    1. Ajout de la méthode `hasTakenHolidays` dans `Employee` et de `resetHasTakenHolidays`
5. Supression des méthodes qui ne sont plus utilisées
6. Changer le type de `Employee.role` pour un `enum`
7. Ajouter des exception personalisées
    1. `InvalidRaiseException`
    2. `EmployeeDoesNotWorkHereException`
    3. `NoEmployeeException`
    4. `NotEnoughHolidaysRemainingException`
