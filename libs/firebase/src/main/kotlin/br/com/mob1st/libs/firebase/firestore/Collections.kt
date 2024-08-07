package br.com.mob1st.libs.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.groups() = collection("groups")

fun FirebaseFirestore.users() = collection("users")

fun FirebaseFirestore.members(groupId: String) = groups().document(groupId).collection("members")

fun FirebaseFirestore.memberships(userId: String) = users().document(userId).collection("memberships")

fun FirebaseFirestore.competitions() = collection("competitions")
