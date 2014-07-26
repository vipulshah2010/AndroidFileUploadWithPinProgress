<?php
$target_path1 = "uploads/";
/* Add the original filename to our target path.
Result is "uploads/filename.extension" */
$target_path1 = $target_path1 . basename( $_FILES['uploaded_file']['name']);
if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $target_path1)) {
    echo "The first file ".  basename( $_FILES['uploaded_file']['name']).
    " has been uploaded.";
} else{
    echo "There was an error uploading the file, please try again!";
    echo "filename: " .  basename( $_FILES['uploaded_file']['name']);
    echo "target_path: " .$target_path1;
}
?>