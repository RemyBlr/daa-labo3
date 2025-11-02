>4.1 Pour le champ remark, destiné à accueillir un texte pouvant être plus long qu’une seule ligne, quelle configuration particulière faut-il faire dans le fichier XML pour que son comportement soit correct ? Nous pensons notamment à la possibilité de faire des retours à la ligne, d’activer le correcteur orthographique et de permettre au champ de prendre la taille nécessaire.

En y ajoutant l'attribut `android:inputType` avec les propriétés `"textMultiLine|textCapSentences|textAutoCorrect"` ou encore l'attribut `android:minLines="3"` pour que le champ ait déjà une hauteur minimale.

```xml
<EditText  
    android:id="@+id/commentField"  
    android:layout_width="0dp"  
    android:layout_height="wrap_content"  
    android:inputType="textMultiLine|textCapSentences"  
    android:minLines="3"  
    android:gravity="top|start"  
    android:imeOptions="actionDone"  
    android:layout_marginTop="24dp"  
    android:hint="@string/hint_comment"  
    app:layout_constraintTop_toBottomOf="@id/emailField"  
    app:layout_constraintStart_toStartOf="parent"  
    app:layout_constraintEnd_toEndOf="parent" />
```

>4.2 Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat
permettant par exemple d’afficher 12 juin 1996 à partir d’une instance de Date. Le formatage
des dates peut être relativement différent en fonction des langues, la traduction des mois par
exemple, mais également des habitudes régionales différentes : la même date en anglais
britannique serait 12th June 1996 et en anglais américain June 12, 1996. Comment peut-on
gérer cela au mieux ?

Pour afficher la date sélectionnée via le `DatePicker`, on utilise un `DateFormat` (dans notre cas
`SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())` pour nos standards en suisse) afin de formater
un objet `Date` ou `Calendar` selon les habitudes de la langue et région. On peut ainsi adapter
facilement le format suivant la langue/région.

On peut également utiliser les classes de formatage de date du système (`java.text.DateFormat.getDateInstance(...)`)
qui s’adaptent automatiquement à la locale de l’utilisateur. Cela permet que la date soit affichée
dans le style correct pour l’utilisateur (langue, position jour/mois/année).

>4.3 Veuillez choisir une question en fonction de votre choix d’implémentation :
a. Si vous avez utilisé le DatePickerDialog1 du SDK. En cas de rotation de l’écran du
smartphone lorsque le dialogue est ouvert, une exception android.view.WindowLeaked
sera présente dans les logs, à quoi est-elle due et comment pouvons-nous la corriger ?
b. Si vous avez utilisé le MaterialDatePicker2 de la librairie Material. Est-il possible de limiter
les dates sélectionnables dans le dialogue ? Ceci en particulier pour une date de naissance
car il est peu probable d’avoir une personne née il y a plus de 110 ans ou à une date dans
le futur. Comment pouvons-nous mettre cela en place ?

Ayant utilisé le `DatePickerDialog` du SDK, si l’écran se tourne pendant que le dialogue est ouvert,
on peut rencontrer l’exception `android.view.WindowLeaked`. Cette exception survient parce que le
dialogue est toujours attaché à l’`Activity` précédente qui est détruite/recréée lors de la rotation
et le `Dialog` n’a pas été détaché ou recréé correctement.

Pour corriger cela, on peut utiliser :
- un `DialogFragment` plutôt qu’un simple `DatePickerDialog` (car `DialogFragment` gère automatiquement 
  le cycle de vie lié à la rotation)
- ou bien dans `onDestroy()` ou `onPause()` ferme explicitement le dialogue (par exemple `dialog.dismiss()`
  si il est encore ouvert). Ainsi on évite le « leak » de fenêtre.

Nous avons décider d'utiliser la fermeture manuel car plus simple à implémenter. Il faut faire att-
ention néanmoins de rien oublier tel que le `.dismiss()` au `onDestroy()` de l'activité et autre gestion.

>4.4 Lors du remplissage des champs textuels, vous pouvez constater que le bouton « suivant » présent sur le clavier virtuel permet
> de sauter automatiquement au prochain champ à saisir, cf. Fig. 2. Est-ce possible de spécifier son propre ordre de remplissage du
> questionnaire ? Arrivé sur le dernier champ, est-il possible de faire en sorte que ce bouton soit lié au bouton de validation du questionnaire ?
> Hint : Le champ remark, multilignes, peut provoquer des effets de bords en fonction du clavier virtuel utilisé sur votre smartphone.
> Vous pouvez l’échanger avec le champ e-mail pour faciliter vos recherches concernant la réponse à cette question.

Oui, il est possible de spécifier un ordre personnalisé de remplissage entre les champs textuels. En utilisant l’attribut
`android:nextFocusDown`, `android:nextFocusForward`, etc., ou les attributs `android:imeOptions="actionNext"`
sur les `EditText` pour définir que le bouton « suivant » du clavier passe au champ suivant désiré.

Par exemple, sur le champ email on peut mettre `android:imeOptions="actionNext"` et sur le champ commentaire
`android:imeOptions="actionDone"`, ce qui fait que sur le dernier champ le bouton du clavier devient « Terminer » ou « Done »
et déclenche la validation du formulaire. On peut écouter l’événement `setOnEditorActionListener` sur ce dernier champ pour déclencher
le bouton de validation (par exemple appeler la méthode `onValidate()`) quand l’utilisateur appuie sur « Done ». On garantit un flux
utilisateur fluide de champ en champ et finalement vers la validation.

>4.5 Pour les deux Spinners (nationalité et secteur d’activité), comment peut-on faire en sorte que le premier choix
> corresponde au choix null, affichant par exemple le label « Sélectionner » ? Comment peut-on gérer cette valeur pour ne
> pas qu’elle soit confondue avec une réponse ?

Pour les deux `Spinner` (nationalité et secteur d’activité), si on veut que le premier élément corresponde à une valeur
« non sélectionnée » (par exemple le libellé « Sélectionner »), on peut définir dans le fichier `strings.xml` un élément
« Sélectionner… » ou « – Choisir– ». On crée un tableau (`string-array`) dont le premier élément est ce libellé. Lors de
l’adaptation via `ArrayAdapter`, ce premier élément apparaît comme choix « vide » ou « non défini ».
Ensuite dans le code, lors de la validation on vérifie que `spinner.selectedItemPosition != 0`
(ou que `selectedItem.toString() != "Sélectionner…"`) pour savoir si une vraie valeur a été choisie.
On peut en plus faire en sorte que lorsque le formulaire est initialisé, `spinner.setSelection(0)` soit la
valeur « non sélectionnée ». Donc cette approche évite que le premier élément soit confondu avec une réponse réelle.