from django.urls import path
from .views import *

urlpatterns = [
    path('login', LoginView.as_view()),
    path('register', RegisterView.as_view()),
    path('latency', LatencyTest.as_view())
]
