# Social Networking for Ducks & Humans

O aplicație desktop de socializare, dezvoltată ca proiect academic în cadrul Universității Babeș-Bolyai. Proiectul pune accent pe aplicarea riguroasă a principiilor de software engineering, design patterns și arhitecturi scalabile.

---

## Caracteristici Principale

### Pentru Utilizatori (Rațe & Oameni):
* **Sistem de Autentificare:** Login securizat cu parole criptate.
* **Gestiunea Prieteniilor:** Trimite, acceptă sau refuză cereri de prietenie.
* **Comunicare Real-time:** Mesagerie privată între prieteni.
* **Notificări:** Sistem de alerte pentru activități și cereri de prietenie primite.
* **Evenimente:** Descoperă și participă la evenimente.

### Pentru Administratori:
* **Control Total:** Gestiunea completă a bazei de date de utilizatori.
* **Vizualizare Avansată:** Tabele cu filtrare și paginare pentru datele despre useri.
* **Audit:** Monitorizarea activității pe platformă.

---

## Arhitectură și Design Patterns

Proiectul a fost construit folosind principii de **Clean Coding** și **Domain-Driven Design (DDD)**, fiind structurat pe straturi (**Layered Architecture**):
1.  **UI Layer:** JavaFX (Interfață grafică).
2.  **Service/Business Layer:** Logica aplicației.
3.  **Repository Layer:** Persistența datelor folosind PostgreSQL.
4.  **Domain Layer:** Modelele de date.

### Design Patterns Implementate:
* **Observer Pattern:** Utilizat pentru actualizarea automată a ferestrelor (UI) la modificarea datelor.
* **Factory Pattern:** Folosit pentru crearea consistentă a entităților complexe.
* **Strategy Pattern:** Folosit pentru diverse calcule utilitare si strategi de validare a entitatilor

---

## Stack Tehnologic

* **Limbaj:** Java
* **UI Framework:** JavaFX
* **Build Tool:** Gradle
* **Database:** PostgreSQL
* 

## 🎓 Scopul Proiectului
Acest proiect a fost realizat în cadrul cursului de **Metode Avansate de Programare** pentru a aprofunda conceptele de arhitectură software și design orientat pe obiecte.
