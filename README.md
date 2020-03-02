# Tous mes Points
### Projet de site à destination des commerçants de puteaux et leurs clients.
<ul>L'application permet:
    <li>d’éviter l’oubli des cartes de fidélités pour les clients ; </li>
    <li>d’éviter la multiplication des cartes inutilement ; </li>
    <li>de diminuer la consommation de papier ;</li>
    <li>de proposer une interface pour que les clients et les commerçants puissent : 
    <ul>
        <li>inscrire leur commerce
        <li>prendre une carte fidélité
        <li>récupérer une récompense une fois la carte fidélité remplie
        </ul>
        </li>
    <li>de proposer aux clients la distance en temps et en km des boutiques </li>
    </ul>

#### Architecture de l'application
<p>Le projet est découpé en microservices:
<ul>
    <li>config-server: appelle un repo git avec les configurations des serveurs</li>
    <li>eureka-server: lie les services avec eureka</li>
    <li>zuul-server: API Gateway</li>
    <li>microservice-merchants: services contient les boutiques, BDD testMmerchants</li>
    <li>microservice-users: services contient les utilisateurs, newsletter et roles, BDD testMusers</li>
    <li>microservice-rewards: services contient les cartes de fidélités, BDD testMrewards</li>
    <li>microservice-mailings: services gère l'envoi de mail</li>
    <li>microservice-clientui: services web servant à l'affichage des données du site</li>
</ul>

### Deploiement

<ol>
 <li>Installer le JDK d'Oracle 1.8 </li>
 <li>Cloner le répertoire</li>
   <li>Installer Maven version minimum 2.</li>
   <li>Installer Tomcat 9.0.14.</li>
   <li>Créez un repository github pour le dossier config-server-repo</li>
   <li>Dans le service config-server, modifiez src\main\resources\application.properties: 
   spring.cloud.config.server.git.uri= l'adresse de votre repository</li>
   <li>Créer trois de données Postgresql:
           <ul>
               <li>Une pour le microservice-merchants </li>
               <li>Une pour le microservice-rewards </li>
               <li>Une pour le microservice-users </li>
               <li>Une pour le microservice-mailing </li>
           </ul>
      </li>
   <li>Dans votre repo config-server-repo,  modifier pour les quatre microservices les informations suivantes:
   <ul>
      <li>le nom de votre base de données: jdbc.url = jdbc:postgresql://localhost:5432/"nom-de-la-base-de-données" </li>
      <li>votre username: jdbc.username = "username" </li>
      <li>votre mot de passe: jdbc.password = "password" </li>
   </ul>
   </li>
 <li>Faites de même pour les champs flyway:
  <ul>
      <li>le nom de votre de données: flyway.url = jdbc:postgresql://localhost:5432/"nom-de-la-base-de-données" </li>
      <li>votre username: flyway.user = "username" </li>
      <li>votre mot de passe: flyway.password = "password" </li>
   </ul>
 </li> 
 <li>Dans le microservice-users, modifiez src\main\resources\db\migration\V1.2__init_data.sql afin de créér vos propres utilisateurs</li>
 <li>Lancez les microservices:
  <ul>
      <li>config-server</li>
      <li>eureka-server</li>
      <li>zuul-server</li>
      <li>microservice-merchants</li>
      <li>microservice-rewards</li>
      <li>microservice-users</li>
      <li>microservice-mailing</li>
      <li>microservice-clientui</li>
  </ul>
 </li>
 <li>Dans un navigateur rendez vous sur http://localhost:8080/</li>
 <li>Vous pouvez vous connecter avec l'email et mot de passe que vous avez créé dans le init_data.sql du service users</li>
 



