# Programmation d'application mobile - Projet Collusion

Le projet est retrouvable au lien suivant : https://github.com/EstebanEstoc/Collusion


## Membres de l'équipe
Juliette DEGUILLAUME et Esteban ESTOC.  

___

## Explications sur le projet  

### Architecture

Le projet est composé de deux applications :  
- **CollusionContact**, nommée App1 dans le sujet du projet
- **CollusionWeb**, nommée App2 dans le sujet du projet  

### Content provider

Les contacts sont transmis de CollusionContact à CollusionWeb via un content provider. Il est créé et rempli de données dans la première application. La deuxième application y accède pour récupérer les contacts et les envoyer par mail.  

### Envoi de mail

Pour paramétrer l'envoi de mail, il faut aller dans le fichier : `CollusionWeb/app/src/main/java/com/enseirb/collusionweb/MainActivity.java`, et plus particulièrement la fonction `sendEmail(String emailBody)`.  
Il est alors possible de changer la valeur de la variable `String toEmail` avec le mail sur lequel vous souhaitez recevoir la liste des contacts subtilisés.